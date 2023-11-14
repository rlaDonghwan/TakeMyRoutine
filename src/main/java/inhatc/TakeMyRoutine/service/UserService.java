package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.dto.JoinRequest;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.dto.LoginRequest;
import inhatc.TakeMyRoutine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //id 중복체크 회원 가입 구현 시 사용 중복되면 true 리턴
    public boolean idDuplicate(String loginId){
        return userRepository.existsByLoginId(loginId);
    }
    //-------------------------------------------------------------------------------------------------

    //닉네임 중복처리
    public boolean nickNameDuplicate(String nickname){
        return userRepository.existsByNickname(nickname);
    }
    //-------------------------------------------------------------------------------------------------

    //회원가입
    public void join(JoinRequest join) {
        userRepository.save(join.toEntity());
    }
    //-------------------------------------------------------------------------------------------------

    //로그인 처리
    public User login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());
        // loginId와 일치하는 User가 없으면 null return
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if (!user.getPassword().equals(req.getPassword())) {
            return null;
        }
        return user;

    }
    //-------------------------------------------------------------------------------------------------

    //유저아이디를 가져옴
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
    //-------------------------------------------------------------------------------------------------



}
