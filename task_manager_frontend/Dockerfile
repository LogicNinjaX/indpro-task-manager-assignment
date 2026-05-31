FROM nginx:alpine

# remove default nginx page
RUN rm -rf /usr/share/nginx/html/*

# copy your files
COPY . /usr/share/nginx/html

EXPOSE 80