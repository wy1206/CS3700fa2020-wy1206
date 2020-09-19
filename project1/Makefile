JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASS = client.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm -f *.class
