import requests
import wget
import zipfile
import os

class Github:
	# TODO create app superclass for globals
	def __init__(self, daroot: str, artifacts: list):
		self.daroot = daroot
		self.artifacts = artifacts

	def search_username(self, username: str):
		url: str = f"https://api.github.com/users/{username}/repos"
		res = requests.get(url)
		json_data = res.json()
		if not isinstance(json_data, list):
			print("Bad Response")
			return
		for i in range(len(json_data)):
			item = json_data[i]
			if i == len(json_data) - 1:
				print(item["name"])
			else:
				print(f"{item['name']}, ", end="")

	def search_repo(self, username: str, repo: str, tag: str = None):
		url: str = f"https://api.github.com/repos/{username}/{repo}/tags"
		res = requests.get(url)
		json_data = res.json()
		if not isinstance(json_data, list):
			print("Bad Response")
			return
		tags = []
		for item in json_data:
			tags.append(item["name"])
		if tag != None:
			if tag in tags:
				print(f"Found {tag}")
		else:
			for i in range(len(tags)):
				item = tags[i]
				if i == len(tags) - 1:
					print(item)
				else:
					print(f"{item}, ", end="")
	
	def pull_repo(self, username: str, repo: str, tag: str):
		extract_to = f"{self.daroot}/github~{username}/{repo}@{tag}"
		print(f"Extract to: {extract_to}")
		if extract_to in self.artifacts:
			print("Already in artifacts")
			return
		url = f"https://api.github.com/repos/{username}/{repo}/zipball/{tag}"
		os.makedirs(f"{self.daroot}/github~{username}", exist_ok=True)
		try:
			filename = wget.download(url, out=f"{self.daroot}/github~{username}/temp.zip")
			
			with zipfile.ZipFile(filename, "r") as zip_ref:
				for member in zip_ref.namelist():
					if member.endswith("/"):
						continue
					parts = member.split("/", 1)
					if len(parts) > 1:
						relative_path = parts[1]
					else:
						relative_path = parts[0]
					target_path = os.path.join(extract_to, relative_path)
					os.makedirs(os.path.dirname(target_path), exist_ok=True)
					with zip_ref.open(member) as src, open(target_path, "wb") as dst:
						dst.write(src.read())

			os.remove(filename)
			return True
		except:
			return False
