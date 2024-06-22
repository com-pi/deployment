FROM mysql:8.4
COPY initdb.d /docker-entrypoint-initdb.d
CMD ["mysqld", "--lower_case_table_names=1"]
