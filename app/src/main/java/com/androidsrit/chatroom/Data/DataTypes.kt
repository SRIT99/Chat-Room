package com.androidsrit.chatroom.Data

data class userData(
    var userId : String? = "",
    var userName: String? = "",
    var number: String? = "",
    var email: String? = "",
    var imgUrl: String? = "",
){
    fun toMap() = mapOf(
        "userId" to userId,
        "userName" to userName,
        "number" to number,
        "email" to email,
        "imgUrl" to imgUrl
    )


}
data class ChatData(
    val chatId : String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
){

}
data class ChatUser(
    var userId: String? = "",
    var name: String? = "",
    var imgUrl: String? = "",
    var number : String? = ""

)