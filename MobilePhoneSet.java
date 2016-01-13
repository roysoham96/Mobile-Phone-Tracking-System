public class MobilePhoneSet
{
	Myset a;
	public MobilePhoneSet()
	{
		a = new Myset();
	}

	public Myset returnMySet()
	{
		return a;
	}

	public boolean isEmpty()
	{
		return a.isEmpty();
	}

	public void InsertMobilePhone(MobilePhone b)
	{
		a.Insert(b);
	}
	
	public void DeleteMobilePhone(MobilePhone b)
	{
		a.Delete(b);
	}

	public Node returnTop()
	{
		return a.top;
	}

	public boolean isMember(MobilePhone b)	//Searching by MobilePhone
	{
		return a.isMember(b);
	}

	public boolean isMember(int num)	//Searching by number
	{
		Node temp = new Node();
		temp = returnTop();
		while (temp!=null)
		{
			MobilePhone b = (MobilePhone) temp.data;
			if (b.number()==num)
				return true;
			else
				temp = temp.next;
		}
		return false;
	}

	public MobilePhone returnMobilePhoneFromNumber(int num)
	{
		Node temp = new Node();
		temp = returnTop();
		while (temp!=null)
		{
			MobilePhone b = (MobilePhone) temp.data;
			if (b.number()==num)
				return b;
			else
				temp = temp.next;
		}
		return null;
	}

	public MobilePhoneSet Union(MobilePhoneSet b)
	{
		MobilePhoneSet c = new MobilePhoneSet();
		c.a = a.Union(b.returnMySet());
		return c;
	}

	public MobilePhoneSet Intersection(MobilePhoneSet b)
	{
		MobilePhoneSet c = new MobilePhoneSet();
		c.a=a.Intersection(b.returnMySet());
		return c;
	}
}
