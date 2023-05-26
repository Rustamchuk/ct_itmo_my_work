package queue;

import java.util.Arrays;

public class LinkedQueue extends AbstractQueue {
    private QueueMember head;
    private QueueMember tail;

    // Pre: True
    // Post: last = element
    protected void addElement(final Object element) {
        if (isEmpty()) {
            head = new QueueMember(element);
            tail = head;
        } else {
            tail.next = new QueueMember(element);
            tail = tail.next;
        }
    }

    // Pre: true
    // Post: R = head
    protected Object getHead() {
        return head.element;
    }

    // Pre: true
    // Post: head = next
    protected void updateHead() {
        head = head.next;
    }

    // Pre: true
    // Post: head = null
    protected void clearValues() {
        head = null;
    }

    // Pre: true
    // Post: R = tail
    protected Object getTail() {
        return tail.element;
    }

    private static class QueueMember {
        private final Object element;
        private QueueMember next;

        private QueueMember(Object element) {
            assert element != null;
            this.element = element;
        }
    }
}
