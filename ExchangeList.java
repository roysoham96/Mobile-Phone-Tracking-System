public class ExchangeList
{
	Exchange top;
	public ExchangeList()
	{
		top=null;
	}

	public int numExchanges()
	{
		int count=0;
		Exchange temp = top;
		while (temp!=null)
		{
			temp=temp.next;
			count++;
		}
		return count;
	}

	public Exchange returnExchange(int i)
	{
		Exchange temp = top;
		for (int x=0; x<i; x++)
		{
			temp=temp.next;
		}
		return temp;
	}

	public Exchange returnTop()
	{
		return top;
	}

	public boolean isEmpty()
	{
		return (top==null);
	}

	public void setTop(Exchange a)
	{
		top = a;
	}

	public boolean isMember(Exchange a)	//Search by Exchange
	{
		Exchange temp = top;
		while (temp!=null)
		{
			if (temp.returnid()==a.returnid())
				return true;
			temp=temp.next;
		}
		return false;
	}

	public boolean isMember(int num)	//Search by ID
	{
		Exchange temp = top;
		while (temp!=null)
		{
			if (temp.returnid()==num)
				return true;
			temp=temp.next;
		}
		return false;
	}

	public void Insert (Exchange a)
	{
		a.next=top;
		top=a;
	}

	public void Delete (Exchange a) throws MyException
	{
		if (!isMember(a))
			throw new MyException();
		else
		{
			if (top.next==null && top.returnid()==a.returnid())	//Only 1 element and matching
				top=null;
			else
			{
				Exchange temp = top;
				while (temp.next!=null)
				{	
					if (temp.next.returnid()==a.returnid())
					{
						temp.next=temp.next.next;
						break;
					}
				}
			}
		}
	}
}