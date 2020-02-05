package fcfs;

public class Process implements Comparable<Object>{

	protected int pid;
	protected int AT;               //Arrival time  �غ� ť�� ���μ����� �����ϴ� �ð�. ���� �ð�
	protected int RT;            //Burst time ����Ʈ �ð�. ���μ����� �۾��� �Ϸ��ϴ� ���� �ʿ��� CPU�� �����ð�
	protected int WT;            //Wait time     �غ� ���¿��� dispatch�� ��ٸ��� �ð�
	protected int TT;            //Total time    ��� ���μ����� ó���ϴ� ���� �ɸ��� �ð� Run time+Wait time
	protected int ATcheck;          //�������� �ð��� AT�� ���ϱ� ���� ���� ����
	protected int RTcheck;       //�������� �ð��� RT�� ���ϱ� ���� ���� ����
	protected int index;		      //AT ������������ ���� ���� ���� index
	protected boolean flag=false;   //AT�� ������ �غ� ť�� �����ߴ����� ���� ����
	
	
	@Override             //Sort�� AT�� ���� ������ ����
	public int compareTo(Object o) {
		Process p = (Process)o;
		return Integer.compare(this.AT, p.AT);	
	}	
}


