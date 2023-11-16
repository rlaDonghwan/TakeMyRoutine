package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.repository.GroupListRepository;
import inhatc.TakeMyRoutine.repository.TodoGroupRepository;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TodoGroupService {
    private final TodoRepositroy todoRepository;
    private final TodoGroupRepository todoGroupRepository;
    private final UserRepository userRepository;
    private final GroupListRepository groupListRepository;

    //그룹을 추가하는 메서드
    public List<TodoGroup> insertGroup(Long userId, List<Long> todoIds, String groupTitle, String groupCategory) {
        // 새로운 그룹 생성
        TodoGroup todoGroup = new TodoGroup();
        todoGroup.setGroupName(groupTitle);
        todoGroup.setCategory(groupCategory);

        // 해당 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        todoGroup.setUser(user);

        // GroupList 생성 및 설정
        List<GroupList> groupLists = new ArrayList<>();
        for (Long todoId : todoIds) {
            Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("Todo not found"));

            GroupList groupList = new GroupList();
            groupList.setTodoGroup(todoGroup);
            groupList.setTodo(todo);
            groupLists.add(groupList);
        }

        // GroupList를 TodoGroup에 설정
        todoGroup.setGroupLists(groupLists);

        // 사용자의 그룹 목록에 새로운 그룹 추가
        user.getTodoGroups().add(todoGroup);

        // 그룹 저장
        todoGroupRepository.save(todoGroup);

        // 사용자 엔터티 저장
        userRepository.save(user);

        // 저장된 그룹 목록 반환
        return user.getTodoGroups();
    }
    //-------------------------------------------------------------------------------------------------

    // 해당 유저가 공유 가능한 그룹 목록을 가져오는 메서드
    public List<TodoGroup> getShareableGroupList() {
        // share가 true인 그룹들만 필터링
        return todoGroupRepository.findByShareTrue();
    }
    //-------------------------------------------------------------------------------------------------

    //그룹 이름 카테고리를 수정하는 메서드
    public void updateGroup(Long groupId, String updatedGroupName, String updatedGroupCategory) {
        // TodoGroup 엔터티 조회
        Optional<TodoGroup> optionalTodoGroup = todoGroupRepository.findById(groupId);

        optionalTodoGroup.ifPresent(todoGroup -> {
            // TodoGroup을 찾았으면 업데이트
            todoGroup.setGroupName(updatedGroupName);
            todoGroup.setCategory(updatedGroupCategory);

            // 업데이트된 TodoGroup을 저장
            todoGroupRepository.save(todoGroup);
        });
    }
    //-------------------------------------------------------------------------------------------------

    //그룹을 삭제하는 메서드
    public void deleteGroup(List<Long> groupId) {
        todoGroupRepository.deleteByIdIn(groupId);
    }
    //-------------------------------------------------------------------------------------------------

    //그룹을 공유하는 메서드
    public void shareGroups(List<Long> groupIds) {
        for (Long groupId : groupIds) {
            Optional<TodoGroup> optionalTodoGroup = todoGroupRepository.findById(groupId);
            optionalTodoGroup.ifPresent(todoGroup -> {
                todoGroup.setShare(true);
                todoGroupRepository.save(todoGroup);
            });
        }
    }
    //-------------------------------------------------------------------------------------------------

    // 그룹 리스트를 가져오는 메서드
    public List<TodoGroup> getGroupList() {
        return todoGroupRepository.findAll();
    }
    //-------------------------------------------------------------------------------------------------




}
