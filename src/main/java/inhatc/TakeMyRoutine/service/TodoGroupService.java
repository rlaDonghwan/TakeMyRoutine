package inhatc.TakeMyRoutine.service;

import inhatc.TakeMyRoutine.domain.Todo;
import inhatc.TakeMyRoutine.domain.TodoGroup;
import inhatc.TakeMyRoutine.repository.TodoGroupRepository;
import inhatc.TakeMyRoutine.repository.TodoRepositroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoGroupService {
    private final TodoRepositroy todoRepository;
    private final TodoGroupRepository todoGroupRepository;

    public void insetGroup(List<Todo> todoIds, String routineTitle, String routineCategory) {
        // 여기에 그룹 추가 로직을 추가
        // 새로운 그룹 생성    //이름 수정 필요
        TodoGroup todoGroup = new TodoGroup();
        todoGroup.setGroupName(routineTitle); //제목추가
        todoGroup.setCategory(routineCategory); // 카테고리추거

        // 선택된 Todo들을 찾아서 그룹에 추가
        List<Todo> todos = todoRepository.findAllByIds(todoIds);
        todoGroup.setTodos(todos);

        // 그룹 저장
        todoGroupRepository.save(todoGroup);
    }

}
