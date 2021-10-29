public class IntStack {
    private int[] stack;
    private int index;

    public IntStack() {
        this.stack = new int[1];
        this.index = -1;
    }

    public int pop() {
        if (isEmpty())
            return 0;
        index--;
        return stack[index+1];
    }

    public void push(int element) {
        if(this.index == this.stack.length - 1)
            increaseSizeStack();
        this.index++;
        this.stack[this.index] = element;
    }

    public int peec() {
        if (isEmpty())
            return 0;
        return this.stack[this.index];
    }

    public int size() {
        return this.index + 1;
    }

    public boolean isEmpty() {
        if (this.index == -1)
            return true;
        return false;
    }

    public void clear() {
        this.index = 0;
    }

    private void increaseSizeStack() {
        int[] newStack;
        newStack = new int[this.stack.length + 1];
        for (int i = 0; i < this.stack.length; i++)
            newStack[i] = this.stack[i];
        this.stack = newStack;
    }
}
