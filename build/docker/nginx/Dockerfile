FROM nginx:latest

COPY certificate-api.crt /etc/ssl/api/certificate.crt
COPY private-api.key /etc/ssl/api/private.key
COPY certificate-bucket.crt /etc/ssl/bucket/certificate.crt
COPY private-bucket.key /etc/ssl/bucket/private.key

COPY ./nginx.conf /etc/nginx/nginx.conf
EXPOSE 80 443