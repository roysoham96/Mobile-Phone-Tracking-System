public class Exchange
{
	int id;
	Exchange parent, next;
	ExchangeList children;
	MobilePhoneSet a;

	public Exchange(int number)
	{
		parent = null;
		a = new MobilePhoneSet();
		id = number;
		children = new ExchangeList();
	}

	public int returnid()
	{
		return id;
	}

	public boolean isRoot()
	{
		return (parent==null);
	}

	public void setParent(Exchange a)
	{
		parent = a;
	}

	public Exchange parent()
	{
		return parent;
	}

	public int numChildren()
	{
		return children.numExchanges();
	}

	public Exchange child(int i)
	{
		return children.returnExchange(i);
	}

	public Exchange returnExchangeFromID(int a)
	{
		if (id==a)
			return this;
		else
		{
			int n = numChildren();
			for (int i=0; i<n; i++)
			{
				if (child(i).returnExchangeFromID(a)!=null)
					return child(i).returnExchangeFromID(a);
			}
			return null;
		}
	}

	public boolean containsExchange(Exchange a)	//Comparing by Exchange
	{
		Exchange curr = this;
		if (curr.returnid()==a.returnid())
			return true;
		else
		{
			int n = curr.numChildren();
			for (int i=0; i<n; i++)
			{
				if (child(i).containsExchange(a))
					return true;
			}
			return false;
		}
	}

	public boolean containsExchange(int a)	//Comparing by ExchangeID
	{
		Exchange curr = this;
		if (curr.returnid()==a)
			return true;
		else
		{
			int n = curr.numChildren();
			for (int i=0; i<n; i++)
			{
				if (child(i).containsExchange(a))
					return true;
			}
			return false;
		}
	}

	public boolean isLeaf()
	{
		int n = numChildren();
		if (n==0)
			return true;
		else
			return false;
	}

	public MobilePhoneSet residentSet()
	{
		return a;
	}

	public boolean findInResidentSet(MobilePhone a)	//Searching by MobilePhone
	{
		return residentSet().isMember(a);
	}

	public boolean findInResidentSet(int num)	//Searching by number
	{
		return residentSet().isMember(num);
	}

	public void printResidentSet()
	{
		MobilePhoneSet s = residentSet();
		Myset c = s.returnMySet();
		int n = c.numNodes();
		MobilePhone m;
		for (int i=n-1; i>0; i--)
		{
			m = (MobilePhone) c.returnNode(i).data;
			if (m.status())
				System.out.print(m.number()+ ", ");
		}
		m = (MobilePhone) c.returnNode(0).data;
		System.out.println(m.number());
	}
}