package com.ahchim.android.myutility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Stack 자료구조 클래스
 *
 * @author Ahchim
 *@param <T>
 */
public class Stack<T> {
	private Node first = null;

	// 비어있는지 체크
	public boolean isEmpty(){
		return first == null;
	}

	// 맨 위의 값을 가져오고 해당 값을 삭제한다.
	public T pop(){
		if(!isEmpty()){
			T temp = first.value;
			first = first.next;
			return temp;
		} else{
			try { // 비어있으면 에러 메세지 호출
				throw new Exception("stack이 비어 있어 pop할 수 없습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// 맨 위의 값을 가져온다.
	public T peek(){
		if(!isEmpty()){
			T temp = first.value;
			return temp;
		} else{
			try { // 비어있으면 에러 메세지 호출
				throw new Exception("stack이 비어 있어 peek할 수 없습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	// 맨 위에 값을 하나 추가한다.
	public void push(T o){
		first = new Node(o, first);
	}

	// 스택 데이터의 개수를 반환한다.
	public int size(){
		return first.countNode(first);
	}

	// 해당 포지션의 스택 아이템을 가져온다. (1부터 시작)
	public T search(int pos){
		// 노드 포지션과 stack 포지션이 반대되므로..
		pos = size() - pos;
		return first.getNode(first, pos).value;
	}

	// 스택 내용 모두 출력하는 함수
	public void print(){
		Node temp = first;
		if(!isEmpty()){
			System.out.println("===Stack===");
			System.out.println("[" + temp.value + "]");
			temp = temp.next;

			while(temp != null){
				System.out.println("[" + temp.value + "]");
				temp = temp.next;
			}
			System.out.println("===========");
		}
	}

	// 스택을 ArrayList로 바꾸어 줍니다.
	public List<T> toArrayList(){
		Node temp = first;
		List<T> listA = new ArrayList<>();

		if(!isEmpty()){
			listA.add(temp.value);
			temp = temp.next;

			while(temp != null){
				listA.add(temp.value);
				temp = temp.next;
			}
		}
		Collections.reverse(listA);
		return listA;
	}

	// 스택을 LinkedList로 바꾸어 줍니다.
	public List<T> toLinkedList(){
		Node temp = first;
		List<T> listL = new LinkedList<>();

		if(!isEmpty()){
			listL.add(temp.value);
			temp = temp.next;

			while(temp != null){
				listL.add(temp.value);
				temp = temp.next;
			}
		}
		Collections.reverse(listL);
		return listL;
	}

	// 일방향 LinkedList로 구성되는 Node 클래스
	class Node{
		public Node next;
		public T value;

		public Node(T value){
			this(value, null);
		}
		public Node(T value, Node next){
			this.next = next;
			this.value = value;
		}

		public int countNode(Node head){
			if (head == null) {
				return 0;
			} else {
				return countNode(head.next) + 1;
			}
		}

		public Node getNode(Node head, int pos){
			int i = 0;
			Node temp = null; temp = head;

			while(temp != null && ++i < pos){
				temp = temp.next;
			}

			return temp;
		}
	}
}
