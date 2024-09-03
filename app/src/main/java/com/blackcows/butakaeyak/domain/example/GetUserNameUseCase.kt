package com.blackcows.butakaeyak.domain.example

import javax.inject.Inject

//UseCase Example!!
class GetUserNameUseCase @Inject constructor(
    //private val userRepository: UserRepository  //TODO: 이용하는 Repository
) {
    //TODO: 무조건 Invoke를 이용한다.
    operator fun invoke(
        id: String,     //필요한 프로퍼티 넣기
        callback: (String) -> (Unit)) {     // 람다로 callback 함수 넣기
        //userRepository.getUserName(id, callback)      //repository가 아직 미구현이기 때문에 주석처리
        callback("Mock Data: UserName")     //테스트를 위해 Mock 데이터 넣어주기
    }
}