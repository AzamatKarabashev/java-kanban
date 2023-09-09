package ru.practicum.tasks.manager;

import ru.practicum.tasks.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node first;
    private Node last;

    /**
     * вызывает инкапсулированный, приватный метод getTasks();
     * по сути, возвращает List просмотренных тасок/сабтасок/эпиков
     */
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * берет из historyMap таски, заполняет ими List и возвращает его соответственно
     */
    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        if (!historyMap.isEmpty()) {
            for (Node value : historyMap.values()) {
                if (value.value != null) {
                    tasks.add(value.value);
                }
            }
        }
        return tasks;
    }

    /**
     * принимает на вход таску/сабтаску/эпик
     * удаляет таску/сабтаску/эпик, если таковая уже добавлена в historyMap, лишая нас дублирования
     * вызывает метод customLinkLast и линкует* таску/сабтаску/эпик
     * кладет в historyMap ключ uniqueId таски/сабтаски/эпика и саму таску/сабтаску/эпик
     */
    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getUniqueId());
            customLinkLast(task);
            historyMap.put(task.getUniqueId(), last);
        }
    }

    /**
     * аналог linkLast из стандартной библиотеки java
     * получает таску/сабтаску/эпик, создает ноду, включая в нее полученную таску/сабтаску/эпик
     * ссылается на предыдущую если не единственный элемент, в ином случае становится первым элементом
     */
    private void customLinkLast(Task task) {
        Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    /**
     * Удаляет ноду по ключу (uniqueId)
     */
    @Override
    public void remove(int uniqueId) {
        Node node = historyMap.remove(uniqueId);
        if (node != null) {
            removeNode(node);
        }
    }

    /**
     * Самый сложный метод, который, я надеюсь, правильно линкует ноды в случае удаления одной из них
     */
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

    private static class Node {
        private final Task value;
        private Node prev;
        private Node next;

        public Node(Task value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }
}
