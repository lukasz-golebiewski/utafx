1. install m2eclipse, m2extras, svn-client (linux already has)
2. copy settings.xml to ~/.m2 
3. Eclipse --> Window --> Preferences --> Maven --> User Settings --> (Select ~/.m2/settings.xml)
4. mkdir somedir/implementation
5. cd somedir/implementation
6. svn co http://utafx.googlecode.com/svn/trunk/implementation/
7. Eclipse --> Import --> Existing Maven project into workspace --> Browse --> (Select xxx/somedir/implementation) --> Check All Projects
8. implementation/pom.xml --> Run As --> Maven build ... --> goals: "clean install"  