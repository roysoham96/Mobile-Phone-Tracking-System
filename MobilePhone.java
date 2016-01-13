public class MobilePhone
{
	int id;
	boolean switched;
	Exchange base;
	
	public MobilePhone(int number)
	{
		id = number;
	}

	public void setlocation(Exchange a)
	{
		base=a;
	}

	public int number()
	{
		return id;
	}

	public boolean status()
	{
		return switched;
	}

	public void switchOn() throws MyException
	{
		switched = true;
	}

	public void switchOff() throws MyException
	{
		if (!switched)
			throw new MyException();
		else
			switched = false;
	}

	public Exchange location() throws MyException
	{
		if (status())
			return base;
		else
			throw new MyException();
	}
}
