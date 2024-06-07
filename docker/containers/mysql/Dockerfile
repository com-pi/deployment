FROM mysql:8.0
ENV MYSQL_ROOT_PASSWORD=1234
COPY ./initdb.d /docker-entrypoint-initdb.d
CMD ["mysqld", "--lower_case_table_names=1"]
