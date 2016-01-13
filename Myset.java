public class Myset
{
	Node top;
	public Myset()
	{
		top=null;
	}

	public int numNodes()
	{
		int count=0;
		Node temp = new Node();
		temp = top;
		while (temp!=null)
		{
			temp=temp.next;
			count++;
		}
		return count;
	}

	public Node returnNode(int i)
	{
		Node temp = new Node();
		temp = top;
		for (int x=0; x<i; x++)
		{
			temp=temp.next;
		}
		return temp;
	}

	public Node returnNodeFromMobilePhone (MobilePhone a)
	{
		Node temp = new Node();
		temp = top;
		while (temp!=null)
		{
			if (temp.data==a)
				break;
			temp=temp.next;
		}
		return temp;
	}

	public Node returnTop()
	{
		return top;
	}

	public boolean isEmpty()
	{
		return (top==null);
	}

	public void setTop(Node a)
	{
		top = a;
	}

	public boolean isMember(Object o)
	{
		Node temp = new Node();
		temp = top;
		while (temp!=null)
		{
			if (temp.data==o)
				return true;
			temp=temp.next;
		}
		return false;
	}

	public void Insert (Object o)
	{
		Node temp = new Node();
		temp.data=o;
		temp.next=top;
		top=temp;
	}

	public void Delete(Object o)
	{
		if (top.next==null && top.data==o)	//Only 1 element and matching
			top=null;
		else
		{
			Node temp = top;
			while (temp.next!=null)
			{
				if (temp.next.data==o)
				{
					temp.next=temp.next.next;
					break;
				}
				else
					temp=temp.next;
			}
		}
	}

	public Myset Union(Myset a)
	{
		Myset b = new Myset();
		b.setTop(a.returnTop());
		Node temp = top;
		while (temp!=null)
		{
			if (!b.isMember(temp.data))
				b.Insert(temp.data);
			temp = temp.next;
		}
		return b;
	}

	public Myset Intersection(Myset a)
	{
		Myset b = new Myset();
		Node temp = top;
		while (temp!=null)
		{
			if (a.isMember(temp.data))
				b.Insert(temp.data);
			temp = temp.next;
		}
		return b;	
	}
}