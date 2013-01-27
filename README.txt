Для Развертывания серверной части необходимо: 

- postgresql-9.1(port 5432) c созданной таблой "pm" для пользователя "postgres", пароль: "postgres"
- mongodb ver ~ 2.2.2 на порту 27017

- Jboss As 7.1.1 с доп. настройками:
1) в modules добавлен модуль "org.postgresql" c драйвером postgresql-9.1-903.jdbc4.jar или более новым
2) в файл standalone.xml добавлен datasource для постгреса:

			   <datasource jta="true" jndi-name="java:jboss/datasources/PostgresqlDS" pool-name="PostgresqlDS" enabled="true" use-java-context="true" use-ccm="true">
                    <connection-url>jdbc:postgresql://localhost:5432/pm</connection-url>
                    <driver>postgresql</driver>
                    <security>
                        <user-name>postgres</user-name>
                        <password>postgres</password>
                    </security>
                </datasource>

	и драйвер к нему:
 				<driver name="postgresql" module="org.postgresql">
                	<xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
                 </driver>
 