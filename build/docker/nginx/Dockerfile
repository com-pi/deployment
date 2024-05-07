FROM nginx:latest

COPY ./certificate.crt /etc/ssl/certificate.crt
COPY ./private.key /etc/ssl/private.key

COPY ./nginx.conf /etc/nginx/nginx.conf
EXPOSE 80 443