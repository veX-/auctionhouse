######## Compile ########
cd src
pwd
javac -cp "./*;$AXISCLASSPATH" AuctionHouseService.java ConnectionManager.java DatabaseInfo.java

######## Install ########
cp *.class $AXIS_HOME/WEB-INF/classes/
java -cp $AXISCLASSPATH org.apache.axis.client.AdminClient -lhttp://localhost:8080/axis/services/AdminService wsc/deploy.wsdd

cd $TOMCAT_HOME/bin
pwd
./startup.sh
