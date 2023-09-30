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

    public boolean nickNameDuplicate(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    public void join(JoinRequest join) {
        userRepository.save(join.toEntity());
    }

    public User login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(!user.getPassword().equals(req.getPassword())) {
            return null;
        }

        return user;
    }

    /**
     * userId(Long)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재하면 User return
     */
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     */
    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }


}


//    @Transactional
//    public String join(User user) {
//        validateDuplicateUser(user);
//        userRepository.save(user);
//        return user.getId();
//    }
//
//    private void validateDuplicateUser(User user) {
//        Optional<User> findUsers = userRepository.findById(user.getId());
//        if (findUsers.isPresent()) {
//            throw new IllegalStateException("중복되는 아이디 입니다..");
//        }
//    }


    //    public UserForm login(UserForm user){
//        Optional<User> findId = userRepository.findUserById(user.getId());
//        if(findId.isPresent()){ // 아이디가 있으면 실항
//            User loginUser = findId.get();
//            if(loginUser.getPassword().equals(user.getPassword())){
//                return UserForm.toUserForm(loginUser);
//            }else {
//                throw new IllegalStateException("중복되는 아이디 입니다..");
//            }
//        }else{
//            throw new IllegalStateException("중복되는 아이디 입니다..");
//        }
//    }
//    public JoinRequest login(JoinRequest form) {
//        Optional<User> userOptional = userRepository.findById(form.getId());
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            if (user.getPassword().equals(form.getPassword())) {
//                // 로그인 성공
//                return new JoinRequest(user.getId(), user.getPassword(), user.getName(), user.getNickName());
//            }
//        }
//
//        // 로그인 실패
//        return null;
//    }



