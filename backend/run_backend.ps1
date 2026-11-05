Set-Location "C:\Users\LENOVO\Desktop\spring\gestionAlumni"
$env:JAVA_HOME = "C:\Users\LENOVO\Desktop\jdk-17.0.12"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
.\mvnw clean spring-boot:run
