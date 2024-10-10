# Use the official Python image from the Docker Hub
FROM python:3.10-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements.txt file to the container
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code to the container
COPY . .

# Expose port 8080 (Flask runs on 5000 by default, but your app uses port 8080 in Docker)
EXPOSE 8080

# Command to run the application
CMD ["python", "app.py"]
