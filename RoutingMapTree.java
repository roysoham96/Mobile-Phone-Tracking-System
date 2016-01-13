import java.util.*;

public class RoutingMapTree
{
	Exchange root, phones;
	public RoutingMapTree()	//For the overall tree
	{
		phones = new Exchange(-1);
		root = new Exchange(0);
		root.setParent(null);
	}

	public RoutingMapTree(Exchange a)	//For a subtree
	{
		root = a;
	}

	public Exchange returnRoot()
	{
		return root;
	}

	public void switchOn(MobilePhone a, Exchange b) throws MyException
	{
		if (!b.isLeaf())
			throw new MyException();
		else
		{
			if (!a.status())
			{
				a.switchOn();
				a.setlocation(b);
			}
			else
				throw new MyException();
		}
	}

	public void switchOff(MobilePhone a) throws MyException
	{
		if (a.status())
			a.switchOff();
		else
			throw new MyException();
	}

	public int depth(Exchange a)
	{
		if (a.isRoot())
			return 0;
		else
			return 1+depth(a.parent());
	}

	public boolean containsExchange(Exchange a)	//Comparing by Exchange
	{
		return root.containsExchange(a);
	}

	public boolean containsExchange(int a)	//Comparing by ExchangeID
	{
		return root.containsExchange(a);
	}

	public Exchange returnExchangeFromID(int a)	//Assuming Exchange with that ID exists
	{
		return root.returnExchangeFromID(a);
	}

	public void addExchange(int a, int b) throws MyException
	{
		if (!containsExchange(a))
			throw new MyException("addExchange " + a + " " + b + ": Error - " + "No exchange with identifier " + a + " found in the network");
		else if (containsExchange(b))
			throw new MyException("addExchange " + a + " " + b + ": Error - " + "Exchange with identifier " + b + " already exists in the network");
		else
		{
			Exchange f = returnExchangeFromID(a);
			Exchange e = new Exchange(b);
			f.children.Insert(e);
			e.setParent(f);
		}
	}

	public void switchOnMobile(int a, int b) throws MyException
	{
		if (!containsExchange(b))
			throw new MyException("switchOnMobile " + a + " " + b + ": Error - " + " No exchange with identifier " + b + " found in the network");
		else
		{
			Exchange e = returnExchangeFromID(b);
			if (!e.isLeaf())
				throw new MyException("switchOnMobile " + a + " " + b + ": Error - " + " Exchange with identifier " + b + " is not a base station");
			else
			{
				if (!e.findInResidentSet(a))
				{
					if (root.findInResidentSet(a))
					{
						MobilePhone m = root.residentSet().returnMobilePhoneFromNumber(a);
						throw new MyException("switchOnMobile " + a + " " + b + ": Error - " + "Mobile Phone with identifier " + a + " is already switched on in Exchange with identifier " + m.location().returnid());
					}
					else
					{
						MobilePhone m = new MobilePhone(a);
						e.residentSet().InsertMobilePhone(m);
						phones.residentSet().InsertMobilePhone(m);
						m.setlocation(e);
						m.switchOn();
						if (!e.isRoot())
						{
							Exchange p = e.parent();
							p.residentSet().InsertMobilePhone(m);
							while (!p.isRoot())
							{
								p.parent().residentSet().InsertMobilePhone(m);
								p=p.parent();
							}
						}
					}
				}
				else
				{
					MobilePhone m = e.residentSet().returnMobilePhoneFromNumber(a);
					m.switchOn();
					if (!e.isRoot())
					{
						Exchange p = e.parent();
						while (!p.isRoot())
							p=p.parent();
					}
				}
			}
		}
	}

	public void switchOffMobile(int a) throws MyException
	{
		if (!phones.residentSet().isMember(a))
			throw new MyException("switchOffMobile " + a + ": Error - No mobile phone with identifier " + a + " found in the network");
		else if (!root.residentSet().isMember(a))
			throw new MyException("switchOffMobile " + a + ": Error - Mobile Phone with identifier " + a + " is currently switched off");
		else
		{
			MobilePhone m = root.residentSet().returnMobilePhoneFromNumber(a);
			Exchange e = m.location();
			e.residentSet().DeleteMobilePhone(m);
			if (!e.isRoot())
			{
				Exchange p = e.parent();
				p.residentSet().DeleteMobilePhone(m);
				while (!p.isRoot())
				{
					p.parent().residentSet().DeleteMobilePhone(m);
					p=p.parent();
				}
			}
			m.switchOff();
		}
	}

	public void queryNthChild(int a, int b) throws MyException
	{
		if (!containsExchange(a))
			throw new MyException("queryNthChild " + a + " " + b + ": Error - No exchange with identifier " + a + " found in the network");
		Exchange e = returnExchangeFromID(a);
		int n = e.numChildren();
		if (b>n-1)
			throw new MyException("queryNthChild " + a + " " + b + ": Error - Exchange with identifier " + a + " has less than " + b + " children");
		else
		{
			int i = n-b-1;
			System.out.println("queryNthChild " + a + " " + b + ": " + e.child(i).returnid());
		}
	}

	public void queryMobilePhoneSet(int a) throws MyException
	{
		if (!containsExchange(a))
			throw new MyException("queryMobilePhoneSet " + a + ": Error - No exchange with identifier " + a + " found in the network");
		else
		{
			Exchange e = returnExchangeFromID(a);
			System.out.print("queryMobilePhoneSet " + e.returnid() + ": ");
			e.printResidentSet();
		}
	}

	public Exchange findPhone(MobilePhone m)
	{
		try
		{
			return m.location();
		}
		catch(MyException e)
		{

		}
		return null;
	}

	public Exchange lowestRouter(Exchange a, Exchange b)
	{
		int diff;
		if (depth(a)>depth(b))
		{
			diff = depth(a) - depth(b);
			for (int i=0; i<diff; i++)
				a=a.parent();
		}
			
		else
		{
			diff = depth(b) - depth(a);
			for (int i=0; i<diff; i++)
				b=b.parent();
		}
		while (a!=b)
		{
			a=a.parent();
			b=b.parent();
		}
		return a;
	}

	public ExchangeList routeCall(MobilePhone a, MobilePhone b) throws MyException
	{
		ExchangeList l = new ExchangeList();
		if (!a.status())
			throw new MyException("queryRouteCall " + a.number() + " " + b.number() + ": Error - Mobile phone with identifier " + a.number() + " is currently switched off\n");
		else if (!b.status())
			throw new MyException("queryRouteCall " + a.number() + " " + b.number() + ": Error - Mobile phone with identifier " + b.number() + " is currently switched off\n");
		else
		{
			Exchange e = findPhone(a);
			Exchange f = findPhone(b);
			Exchange lr = lowestRouter(e,f);
			Vector<Exchange> temp = new Vector<Exchange> ();
			while (e!=lr)
			{
				l.Insert(e);
				e=e.parent();
			}
			l.Insert(lr);
			while (f!=lr)
			{
				temp.add(f);
				f=f.parent();
			}
			for (int i=temp.size()-1 ; i>=0 ; i--)
			{
				l.Insert(temp.get(i));
			}
			return l;
		}
	}
	public void movePhone(MobilePhone a, Exchange b) throws MyException
	{
		switchOffMobile(a.number());
		phones.residentSet().DeleteMobilePhone(a);
		switchOnMobile(a.number(),b.returnid());
	}

	public void validateCommand(String text) throws MyException
	{
		if ((!text.equals("addExchange"))&&(!text.equals("queryNthChild"))&&(!text.equals("queryMobilePhoneSet"))&&(!text.equals("switchOnMobile"))&&(!text.equals("switchOffMobile"))&&(!text.equals("queryFindPhone"))&&(!text.equals("lowestRouter"))&&(!text.equals("queryFindCallPath"))&&(!text.equals("movePhone")))
			throw new MyException();
	}

	public void performAction(String actionMessage)
	{
		int count=0;
		String text="";
		String arg1="";
		String arg2="";
		int a, b, m, m2;
		int i=0;
		int len = actionMessage.length();
		
		for (int j=0; j<len; j++)
		{	
			if (actionMessage.substring(j, j+1).equals(" "))
				break;
			else
			{
				i++;
				text+=actionMessage.substring(j, j+1);
			}
		}

		try
		{
			validateCommand(text);
			i+=1;
			if (text.equals("addExchange"))
			{
				while (!(actionMessage.substring(i, i+1)).equals(" "))
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				a=Integer.parseInt(arg1);
				i+=1;
				
				while (i<len)
				{
					arg2+=actionMessage.charAt(i);
					i++;
				}
				b=Integer.parseInt(arg2);
				try
				{
					addExchange(a, b);
				}
				catch (MyException p)
				{
					System.out.println();
				}
			}
			else if (text.equals("switchOnMobile"))
			{
				while (!(actionMessage.substring(i, i+1)).equals(" "))
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				m=Integer.parseInt(arg1);
				i+=1;		
				while (i<len)
				{
					arg2+=actionMessage.charAt(i);
					i++;
				}
				b=Integer.parseInt(arg2);
				try
				{
					switchOnMobile(m, b);
				}
				catch (MyException q)
				{
					System.out.println();
				}
			}
			else if (text.equals("switchOffMobile"))
			{
				while (i<len)
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				m=Integer.parseInt(arg1);
				try
				{
					switchOffMobile(m);
				}
				catch (MyException r)
				{
					System.out.println();
				}
			}
			else if (text.equals("queryNthChild"))
			{
				while (!(actionMessage.substring(i, i+1)).equals(" "))
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				a=Integer.parseInt(arg1);
				i+=1;	
				while (i<len)
				{
					arg2+=actionMessage.charAt(i);
					i++;
				}
				b=Integer.parseInt(arg2);
				try
				{
					queryNthChild(a, b);
				}
				catch (MyException s)
				{
					System.out.println();
				}
			}
			else if (text.equals("queryMobilePhoneSet"))	
			{
				while (i<len)
				{
						arg1+=actionMessage.charAt(i);
						i++;
				}
				a=Integer.parseInt(arg1);
				try
				{
					queryMobilePhoneSet(a);
				}
				catch (MyException t)
				{
					System.out.println();
				}
			}
			else if (text.equals("queryFindPhone"))
			{
				while (i<len)
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				m=Integer.parseInt(arg1);
				try
				{
					if (!phones.residentSet().isMember(m))
						throw new MyException("queryFindPhone " + m + ": Error - No mobile phone with identifier " + m + " found in the network");
					else
					{
						MobilePhone s = phones.residentSet().returnMobilePhoneFromNumber(m);
						if (!s.status())
							throw new MyException("queryFindPhone " + m + ": Error - Mobile phone with identifier " + m + " is currently switched off");
						else
							System.out.println("queryFindPhone " + m + ": " + findPhone(s).returnid());
					}
				}
				catch (MyException r)
				{
					System.out.println();
				}
			}
			else if (text.equals("lowestRouter"))
			{
				while (!(actionMessage.substring(i, i+1)).equals(" "))
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				a=Integer.parseInt(arg1);
				i+=1;	
				while (i<len)
				{
					arg2+=actionMessage.charAt(i);
					i++;
				}
				b=Integer.parseInt(arg2);
				try
				{
					if (!containsExchange(a))
						throw new MyException("queryLowestRouter: " + a + "  " + b + ": Error - No exchange with identifier " + a + " found in the network");
					else if (!containsExchange(b))
						throw new MyException("queryLowestRouter: " + a + "  " + b + ": Error - No exchange with identifier " + b + " found in the network");
					else
					{
						Exchange e = returnExchangeFromID(a);
						Exchange f = returnExchangeFromID(b);
						System.out.println("queryLowestRouter " + a + " " + b + ": " + lowestRouter(e,f).returnid());
					}
				}
				catch (MyException s)
				{
					System.out.println();
				}
			}
			else if (text.equals("queryFindCallPath"))
			{
				while (!(actionMessage.substring(i, i+1)).equals(" "))
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				m=Integer.parseInt(arg1);
				i+=1;	
				while (i<len)
				{
					arg2+=actionMessage.charAt(i);
					i++;
				}
				m2=Integer.parseInt(arg2);
				try
				{
					if (!root.residentSet().isMember(m))
					{
						if (phones.residentSet().isMember(m))
							throw new MyException("queryFindCallPath " + m + " " + m2 + ": Error - Mobile phone with identifier " + m + " is currently switched off\n");
						else
							throw new MyException("queryFindCallPath " + m + " " + m2 + ": Error - No mobile phone with identifier " + m + " found in the network\n");
					}
					else if (!root.residentSet().isMember(m2))
					{
						if (phones.residentSet().isMember(m2))
							throw new MyException("queryFindCallPath " + m + " " + m2 + ": Error - Mobile phone with identifier " + m2 + " is currently switched off\n");
						else
							throw new MyException("queryFindCallPath " + m + " " + m2 + ": Error - Mobile phone with identifier " + m2 + " found in the network\n");
					}
					else if (m==m2)
						throw new MyException("queryFindCallPath " + m + " " + m2 + ": Error - It is not possible for phone with identifier " + m + " to call itself\n");
					MobilePhone s1 = root.residentSet().returnMobilePhoneFromNumber(m);
					MobilePhone s2 = root.residentSet().returnMobilePhoneFromNumber(m2);
					ExchangeList k = routeCall(s1, s2);
					int num = k.numExchanges();
					System.out.print("queryFindCallPath " + m + " " + m2 + ": ");
					for (int l=num-1; l>0; l--)
						System.out.print(k.returnExchange(l).returnid() + ", ");
					System.out.print(k.returnExchange(0).returnid());
					System.out.println();
				}
				catch (MyException s)
				{
					
				}
			}
			else if (text.equals("movePhone"))
			{
				while (!(actionMessage.substring(i, i+1)).equals(" "))
				{
					arg1+=actionMessage.charAt(i);
					i++;
				}
				a=Integer.parseInt(arg1);
				i+=1;	
				while (i<len)
				{
					arg2+=actionMessage.charAt(i);
					i++;
				}
				b=Integer.parseInt(arg2);
				try
				{
					if (!phones.residentSet().isMember(a))
						throw new MyException("movePhone " + a + " " + b + ": Error - No mobile phone with identifier " + a + " found in the network");
					else if (!containsExchange(b))
						throw new MyException("movePhone " + a + " " + b + ": Error - No exchange with identifier " + b + " found in the network");
					else
					{
						MobilePhone s1 = phones.residentSet().returnMobilePhoneFromNumber(a);
						if (s1.status())
						{
							Exchange f = returnExchangeFromID(b);
							if (!f.isLeaf())
								throw new MyException("movePhone " + a + " " + b + ": Error - Exchange with identifier " + b + " is not a base station");
							else if (s1.location()==f)
								throw new MyException("movePhone " + a + " " + b + ": Error - Mobile phone with identifier " + a + " is already under base station with identifier " + b);
							else
							{
								movePhone(s1,f);
							}
						}
						else
							throw new MyException("movePhone " + a + " " + b + ": Error -  Mobile phone with identifier " + a + " is currently switched off");
					}
				}
				catch (MyException s)
				{
					System.out.println();
				}
			}	
		}
		catch (MyException e)
		{
			System.out.println(actionMessage + ": Error - Invalid query/command");
		}
	}
}