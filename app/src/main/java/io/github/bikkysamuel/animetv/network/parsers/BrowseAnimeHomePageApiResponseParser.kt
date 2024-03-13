package io.github.bikkysamuel.animetv.network.parsers

import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.utils.HtmlDocumentCssQuery
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class BrowseAnimeHomePageApiResponseParser {

    fun parseResponseAsAnimeList(responseAsHtml: String) : ArrayList<BrowseAnimeItem> {
        val browseAnimeItems: ArrayList<BrowseAnimeItem> = ArrayList()
        val elements: Elements = Elements()

        val document: Document = Jsoup.parse(responseAsHtml)
        elements.addAll(document.select(HtmlDocumentCssQuery.DOC_HOME_ANIME_LIST))

        for (i in 0..<elements.size) {
            val element: Element = elements[i]
            val videoUrl = element.select(HtmlDocumentCssQuery.DOC_HOME_ANIME_ITEM_VIDEO_URL).attr("href")
            var imgUrl = element.select(HtmlDocumentCssQuery.DOC_HOME_ANIME_ITEM_IMAGE_URL).attr("src")
            val name = element.select(HtmlDocumentCssQuery.DOC_HOME_ANIME_ITEM_NAME).text()
            val date = element.select(HtmlDocumentCssQuery.DOC_HOME_ANIME_ITEM_UPLOAD_DATE).text()

            val imgUrlPrefixWithLoadError = "https://cdnimg.xyz"
            if (imgUrl.startsWith(imgUrlPrefixWithLoadError)) {
                val replacementForImgUrlPrefix = "https://gogocdn.net"
                imgUrl = imgUrl.replace(imgUrlPrefixWithLoadError, replacementForImgUrlPrefix)
            }

            val browseAnimeItem: BrowseAnimeItem =
                BrowseAnimeItem(id = i, name = name, description = "", uploadedDate = date,
                    videoUrl = videoUrl, imgUrl = imgUrl)
            browseAnimeItems.add(browseAnimeItem)
        }

        return browseAnimeItems
    }
}