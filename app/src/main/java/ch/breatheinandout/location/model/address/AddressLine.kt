package ch.breatheinandout.location.model.address

data class AddressLine(
    val addr: String,
    val sidoName: String,       // 시도명
    val sggName: String,        // 시도군 이름
    val umdName: String         // 읍면동 이름
)