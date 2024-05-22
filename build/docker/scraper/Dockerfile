FROM python:3.10.12
COPY ./app /app
RUN pip install --no-cache-dir -r app/requirements.txt
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8080"]
