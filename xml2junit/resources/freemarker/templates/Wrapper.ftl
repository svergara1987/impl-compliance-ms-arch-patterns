package ${packageName};

public class Wrapper {
	
<#list operations as anOperation>
	public ${anOperation.returnType} ${anOperation.name} (${anOperation.parametersStrRep?join(", ")}) {
		throw new RuntimeException("code not implemented yet");
	}
		
</#list>
}
