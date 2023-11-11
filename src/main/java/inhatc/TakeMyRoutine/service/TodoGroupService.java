package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.GroupList;
import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.domain.User;
import inhatc.TakeMyRoutine.repository.TodoGroupRepository;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import inhatc.TakeMyRoutine.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoGroupService {
    private final TodoRepositroy todoRepository;
    private final TodoGroupRepository todoGroupRepository;
    private final UserRepository userRepository;

    // TodoGroupService에서 insertGroup 메서드 수정
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

    // 데이터베이스에서 그룹 리스트를 조회하는 메서드
    public List<TodoGroup> getGroupList() {
        return todoGroupRepository.findAll();
    }

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

    public void completeTodos(List<Long> groupId) {
        todoGroupRepository.deleteByIdIn(groupId);
    }


}
