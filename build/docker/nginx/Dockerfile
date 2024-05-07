FROM nginx:latest

COPY certificate.crt /etc/ssl/certifivate.crt
COPY private.key /etc/ssl/private.crt

COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80 443