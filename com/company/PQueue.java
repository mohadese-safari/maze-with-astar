package com.company;

public class PQueue {

    int size = 0;
    int first = 0;
    State[] data;

    PQueue(int capacity) {
        data = new State[capacity];

    }

    PQueue() {
        data = new State[100];
    }

    public void enqueue(State element) {
        if (size == data.length) {
            return;
        }
        data[(first + size) % data.length] = element;
        size++;
        sortLastElm();
    }

    public void sortLastElm() {
        int last = (first + size - 1) % data.length;
        int iterate = size - 1;
        while (iterate > 0 && data[last].getF() <= data[(last + data.length - 1) % data.length].getF()) {
            if (data[last].getF() == data[(last + data.length - 1) % data.length].getF()) {
                if (data[last].getH() > data[(last + data.length - 1) % data.length].getH()) { // در صورت برابری هزینه ها آن که هیوریستیک کمتری دارد جلوتر است
                    break;
                }
            }
            iterate--;
            State temp = data[last];
            data[last] = data[(last + data.length - 1) % data.length];
            data[(last + data.length - 1) % data.length] = temp;
            last = (last + data.length - 1) % data.length;
        }
    }

    public State dequeue() {
        if (size == 0) {
            return null;
        }
        State result = data[(first + size - 1) % data.length];
        size--;
        first++;
        return result;
    }

    public boolean contains(State element) {
        int i = first;
        int iterated = 0;
        while (iterated < size && data[i] != null ) {
            if (data[i] == element) {
                return true;
            }

            i = (i + 1) % data.length;
            iterated++;
        }

        return false;
    }

    public String toString() {
        String result = "";
        int i = first;
        int iterated = 0;
        while (data[i] != null && iterated <= size) {
            result += data[i].getH() + " ";
            i = (i + 1) % data.length;
            iterated++;
        }

        return result;
    }

    public static void main(String[] args) {
        Main.goal = new State(null, 9, 9, 0);
        PQueue q = new PQueue(2);
        State s1 = new State(null, 0, 0, 0);
        State s2 = new State(s1, 1, 0, 1);
        State s3 = new State(s1, 0, 1, 1);

        q.enqueue(s1);
        q.enqueue(s2);
        q.enqueue(s3);

        //q.dequeue();
       // q.dequeue();
      //  q.dequeue();
        System.out.println(q.dequeue());

        System.out.println(q.contains(s1));
        System.out.println(q.contains(s2));
        System.out.println(q.contains(s3));

    }

}
