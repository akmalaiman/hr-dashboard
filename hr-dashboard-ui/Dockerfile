FROM node:20.12.0 AS build

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build --prod

FROM nginx:alpine

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/hr-dashboard-ui/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
