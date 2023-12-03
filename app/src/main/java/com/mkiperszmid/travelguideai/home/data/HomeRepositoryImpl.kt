package com.mkiperszmid.travelguideai.home.data

import com.mkiperszmid.travelguideai.home.data.remote.ChatgptApi
import com.mkiperszmid.travelguideai.home.data.remote.dto.ChatRequestDto
import com.mkiperszmid.travelguideai.home.domain.HomeRepository
import com.mkiperszmid.travelguideai.home.domain.model.HomeFilterSettings
import com.mkiperszmid.travelguideai.home.domain.model.Place
import com.mkiperszmid.travelguideai.home.domain.model.Region

class HomeRepositoryImpl(
    private val api: ChatgptApi
) : HomeRepository {
    override suspend fun getTravelGuide(
        location: String,
        settings: HomeFilterSettings
    ): Result<String> {
        return try {
            var places = ""
            if (settings.restaurant) places += "Cafeterias, "
            if (settings.museums) places += "Reposterias, "

            val placesToVisit = if (places.isNotEmpty()) "y quiero visitar: $places" else ""

            val request = ChatRequestDto(
                maxTokens = 1500,
                model = "text-davinci-003",
                "Soy un fanático del café. Te voy a dar mi ubicación y me vas a sugerir lugares para visitar cerca. También te voy a dar los tipos de lugares que quiero visitar. Aparte quiero que me sugieras lugares de un tipo similar a los que te mencioné que estén cerca de mi primera ubicación. Estoy en $location $placesToVisit: cafeterías y lugares donde disfrutar de un delicioso café, ya sea frío o caliente. Dame la ubicación de cada lugar. Ordénalos desde el más cercano al más lejano de mi ubicación.",
                temperature = 0.7
            )
            val information = api.getTravelInformation(request)
            Result.success(information.choices.first().text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPopularPlaces(): Result<List<Place>> {
        return Result.success(
            listOf(
                Place("", "Café expreso", Region.MEXICO, "https://cdn.pixabay.com/photo/2013/11/05/23/55/coffee-206142_960_720.jpg"),
                Place("", "Café americano", Region.MEXICO, "https://cdn.pixabay.com/photo/2017/09/04/18/39/coffee-2714970_960_720.jpg"),
                Place("", "Café mexicano", Region.MEXICO, "https://cdn.pixabay.com/photo/2016/04/12/11/19/coffee-1324126_960_720.jpg"),
                Place("", "Café capuchino", Region.MEXICO, "https://cdn.pixabay.com/photo/2017/08/07/22/57/coffee-2608864_960_720.jpg"),
                Place("", "Postres Y café", Region.MEXICO, "https://cdn.pixabay.com/photo/2019/11/23/20/04/coffee-4648041_960_720.jpg"),
                Place("", "Granos de café tostados", Region.MEXICO, "https://cdn.pixabay.com/photo/2017/04/25/08/02/coffee-beans-2258839_960_720.jpg")
            )
        )
    }
}
