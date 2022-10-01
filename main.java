import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {
  
/**
* Takes a symbolic/numeric infix expression as input and converts it to
* postfix notation. There is no assumption on spaces between terms or the
* length of the term (e.g., two digits symbolic or numeric term)
*
* @param expression infix expression
* @return postfix expression
*/
  
public String infixToPostfix(String expression);
  
  
/**
* Evaluate a postfix numeric expression, with a single space separator
* @param expression postfix expression
* @return the expression evaluated value
*/
  
public int evaluate(String expression);

}

class MyStack {
    
    static class Node{
        private Object element;
        private Node next;

        public Node() {
            element=null;
            next=null;
        }

        public Node(Object element, Node next) {
            this.element = element;
            this.next = next;
        }

        public Object getElement() {
            return element;
        }

        public void setElement(Object element) {
            this.element = element;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
        
    }
    private Node top;
    private int size;

    public MyStack() {
        top=null;
        size=0;
    }
    
    
    public Object pop() {
        if(isEmpty()==true){
            
            throw new EmptyStackException();
        }
        Object temp=top.getElement();
        top=top.getNext();
        size--;
        return temp;
    }

    public Object peek() throws EmptyStackException{
        if(isEmpty()==true){
            throw new EmptyStackException();
        }
        return top.getElement();
    }

    public void push(Object element) {
       Node v =new Node(element,top);
       top=v;
       size++;
    }

    public boolean isEmpty() {
       return top == null;
    }

    public int size() {
        return size;
    }
    
    public void printstack(){
        Node t=top;
        System.out.print("[");
        while(t!=null){
            System.out.print(t.getElement());
            if(t.next!=null){
                System.out.print(", ");
            }
            t=t.next;
        }
        System.out.print("]");
    }
}


public class Evaluator implements IExpressionEvaluator {
    
    public int prec(char x){
        switch(x){
            case'^':
                return 3;
            case'*':
            case'/':
                return 2;
            case'+':
            case'-':
                return 1;
            default:
                return 0;
        }
    }
        @Override
    public String infixToPostfix(String expression) {
       MyStack s=new MyStack();
       String post="";
       for(int i=0;i<expression.length();i++){
           switch(expression.charAt(i)){
               case'(':
                   s.push('(');
                   break;
                case')':
                    while((char)s.peek()!='('){
                        post+=(char)s.pop();
                    }
                    s.pop();
                    break;
                case'+':
                case'-':
                case'/':
                case'*':
                case'^':
                    if(expression.charAt(i+1)=='+'){
                        expression=expression.substring(0, i+1)+expression.substring(i+2, expression.length());
                    }
                    while(s.isEmpty()==false&&prec((char)s.peek())>=prec(expression.charAt(i))){
                        post+=(char)s.pop();
                    }
                    s.push(expression.charAt(i));
                    break;
                default:
                    post+=expression.charAt(i);
                    break;
           }
       }
       while(s.isEmpty()==false){
               post+=(char)s.pop();
           }
       return post;
    }

    @Override
    public int evaluate(String expression) {
        int a,b,c;
        MyStack t=new MyStack();
        for(int i=0;i<expression.length();i++){
            if(expression.charAt(i)=='{'){
                t.push(Integer.parseInt(expression.substring(i+1, expression.indexOf('}', i+1))));
                i=expression.indexOf('}', i+1);
            }
            else if(expression.charAt(i)=='-'&&t.size()==1){
                c=(int)t.pop();
                t.push(-1*c);
            }
            else {
                a=(int)t.pop();
                b=(int)t.pop();
                switch(expression.charAt(i)){
                    case'+':
                        t.push(b+a);
                        break;
                    case'-':
                        t.push(b-a);
                        break;   
                    case'*':
                        t.push(b*a);
                        break;
                    case'/':
                        t.push(b/a);
                        break;
                    case'^':
                        t.push((int)Math.pow(b, a));
                        break;   
                }
            }
        }
        return(int)t.pop();
    }
  
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        int sign=1;
        int k=0;
        String z="";
        String post="";
        Evaluator ev=new Evaluator();
        String s=input.next().replaceAll("--", "+");
        String ka=input.next();
        String kb=input.next();
        String kc=input.next();
        switch (s.charAt(0)) {
            case '+':
                s=s.substring(1, s.length());
                break;
            case '*':
            case '/':
            case '^':
                System.out.println("Error");
                return; 
        }
        switch (s.charAt(s.length()-1)) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
                System.out.println("Error");
                return; 
        }
        try{
            z=ev.infixToPostfix(s);
        }
        catch(Exception e){
            System.out.println("Error");
            return;
        }
        for(int i=0;i<z.length();i++){
            switch(z.charAt(i)){
                case'a':
                    post+="{";
                    post+=ka.substring(2, ka.length());
                    post+="}";
                    break;
                case'b':
                    post+="{";
                    post+=kb.substring(2, kb.length());
                    post+="}";
                    break;
                case'c':
                    post+="{";
                    post+=kc.substring(2, kc.length());
                    post+="}";
                    break;
                default:
                    post+=z.charAt(i);
                    break;
            }
        }
        try{
            k=ev.evaluate(post);
        }
        catch(Exception e){
            System.out.println("Error");
            return;
        }
        System.out.println(z);
        System.out.println(sign*k);
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. */
    }
}
