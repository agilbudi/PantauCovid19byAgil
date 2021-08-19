package id.budi.agil.covid19.model

data class AllCountries(
        // data object menurut respond dari api
    val Global: World,
    val Countries: ArrayList<Countries>
)
