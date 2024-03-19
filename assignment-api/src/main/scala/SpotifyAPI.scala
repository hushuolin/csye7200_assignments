import io.circe.generic.auto._
import io.circe.parser._
import requests._

case class AccessTokenResponse(access_token: String, token_type: String, expires_in: Int)
case class TrackItem(track: Track)
case class Track(name: String, duration_ms: Int, artists: List[SimpleArtist])
case class SimpleArtist(id: String, name: String)
case class ArtistDetails(followers: Followers)
case class Followers(total: Int)
case class PlaylistTracks(items: List[TrackItem])

object SpotifyAPI {
  val clientID = "your-api-id"
  val clientSecret = "your-api-secret"

  val playlistId = "5Rrf7mqN8uus2AaQQQNdc1"

  def getAccessToken: String = {
    val tokenResponse = requests.post(
      "https://accounts.spotify.com/api/token",
      data = Map(
        "grant_type" -> "client_credentials"
      ),
      auth = (clientID, clientSecret)
    )

    decode[AccessTokenResponse](tokenResponse.text) match {
      case Right(response) => response.access_token
      case Left(error) =>
        println(s"Failed to obtain access token: $error")
        ""
    }
  }

  def getPlaylistTracks(accessToken: String): List[TrackItem] = {
    val response = requests.get(
      s"https://api.spotify.com/v1/playlists/$playlistId/tracks",
      headers = Map("Authorization" -> s"Bearer $accessToken")
    )

    decode[PlaylistTracks](response.text) match {
      case Right(playlistTracks) => playlistTracks.items
      case Left(error) =>
        println(s"Error fetching playlist tracks: $error")
        List.empty
    }
  }

  def getArtistDetails(artistId: String, accessToken: String): Option[ArtistDetails] = {
    val response = requests.get(
      s"https://api.spotify.com/v1/artists/$artistId",
      headers = Map("Authorization" -> s"Bearer $accessToken")
    )

    decode[ArtistDetails](response.text).toOption
  }

  def main(args: Array[String]): Unit = {
    val accessToken = getAccessToken
    if (accessToken.nonEmpty) {
      val tracks = getPlaylistTracks(accessToken)
      val top10LongestTracks = tracks.map(_.track).sortBy(-_.duration_ms).take(10)

      println("Part 1: Top 10 Longest Songs")
      top10LongestTracks.foreach { track =>
        println(s"${track.name}, ${track.duration_ms}")
      }

      println("\nPart 2: Artist Details (Sorted by Followers)")
      val artistDetails = top10LongestTracks
        .flatMap(_.artists)
        .distinctBy(_.id)
        .flatMap(artist => getArtistDetails(artist.id, accessToken).map(details => (artist.name, details.followers.total)))
        .sortBy(-_._2)

      artistDetails.foreach { case (name, followers) =>
        println(s"$name: $followers")
      }
    }
  }
}
