package roundrobin;


public class Queue {
	int MAX_QUEUE_SIZE;
	Process data[];      //프로세스를 넣을 수 있는 큐 배열
	int front;
	int rear;
	Process ERROR = new Process();	//에러시 return시킬 프로세스
	Queue(int max) {
		this.MAX_QUEUE_SIZE = max;
		this.data = new Process[MAX_QUEUE_SIZE];
	}
	//큐 초기화
	public void init_queue(Queue q1){
		q1.front=0;
		q1.rear=0;
		ERROR.pid=0;		
	}	
	
	//공백 상태 검출
	public boolean is_empty(Queue q1) {
		if(q1.front==q1.rear) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//포화 상태 검출
	public boolean is_full(Queue q1) {
		if(q1.front==((q1.rear+1)%MAX_QUEUE_SIZE)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//enqueue
	public void en_queue(Queue q1, Process p1) {
		if(q1.is_full(q1)==true) {
			System.out.println("queue state is full");
		}
		else {		
		q1.rear=(q1.rear +1)%MAX_QUEUE_SIZE;
		q1.data[q1.rear]=p1;
		//System.out.printf("(enqueue %d)", p1.pid);
		}
		
	}
	
	//dequeue
	public Process de_queue(Queue q1) {
		if(q1.is_empty(q1)==true) {
			System.out.println("queue state is empty");
			return ERROR;
		}
		else {
		q1.front=(q1.front +1)%MAX_QUEUE_SIZE;
		return q1.data[q1.front];		
		}
		
	}
		
}




