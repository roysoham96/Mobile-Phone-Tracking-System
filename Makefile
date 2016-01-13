JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
        checker.java \
        Exchange.java \
        ExchangeList.java \
        MobilePhone.java \
        MobilePhoneSet.java \
        MyException.java \
        Myset.java \
        Node.java \
        RoutingMapTree.java 

default: classes
classes: $(CLASSES:.java=.class)
clean:
	rm -f *.class