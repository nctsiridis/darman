# ~/.darrc
# root ~/dev/
# use github.torvals.linux@v1.0.0
# use github.nctsiridis.plife@v1.0.0
# goes in the .github folder

# use nctsiridis.plife@v1.0.0 (this would be in the DB)

import sys
import requests
from types import FunctionType
import os
import zipfile
import wget

daroot = None
artifacts = []

def github_search_username(username: str):
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

def github_search_repo(username: str, repo: str, tag: str = None):
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

def ls_cmd(argv: list):
	print("This is the ls command")

def github_pull(username: str, repo: str, tag: str):
	print(f"daroot: {daroot}")
	extract_to = f"github~{username}.{repo}@{tag}"
	if extract_to in artifacts:
		print("Already in artifacts")
		return
	url = f"https://api.github.com/repos/{username}/{repo}/zipball/{tag}"
	os.makedirs(f"{daroot}/github~{username}")
	filename = wget.download(url, out=f"{daroot}/github~{username}/temp.zip")
	with zipfile.ZipFile(filename, "r") as zip_ref:
			zip_ref.extractall(f"{daroot}/extract_to")

def pull_cmd(argv: list):
	if daroot == None:
		print("Please set daroot in ~/.darrc to use pull")
		return
	query = argv[-1]
	source = None
	if "~" in query:
		source, query = query.split("~")
	username, tagged_repo = query.split("/")
	repo, tag = tagged_repo.split("@")
	if source == "github":
		github_pull(username, repo, tag)

def search_cmd(argv: list):
	query = argv[-1]
	if "/" not in query:
		github_search_username(query)
	elif "@" not in query:
		username, repo = query.split("/")
		github_search_repo(username, repo)
	else:
		username, tagged_repo = query.split("/")
		repo, tag = tagged_repo.split("@")
		github_search_repo(username, repo, tag=tag)

def help_cmd():
	print("This is the help command")

command_tree = {
	"ls": {"*": ls_cmd, "": ls_cmd},
	"pull": {"*": pull_cmd},
	"search": {"*": search_cmd},
	"help": help_cmd
}

def load_global_specs():
	global daroot, artifacts
	path = os.path.expanduser("~/.darrc")
	if not os.path.exists(path):
		open(path, "w").close()
		return None, None
	with open(path, "r") as file:
		file = open(path, "r")
		for line in file:
			if " " not in line:
				continue
			l, r = line.split(" ")
			if l == "root":
				daroot = r
			elif l == "use":
				artifacts.append(r)

def main():
	pos = command_tree;
	load_global_specs();
	for arg in sys.argv[1:]:
		if not isinstance(pos, dict):
			print("Invalid command")
			return
		if pos.get(arg) != None:
			pos = pos[arg]
		elif pos.get("*") != None:
			pos = pos["*"]
		else:
			print("Invalid command")
			return
	if type(pos) != FunctionType:
		if pos.get("") == None:
			print("Invalid command")
			return
		else:
			pos = pos[""]
		if type(pos) != FunctionType:
			print("Invalid command")
			return

	pos(sys.argv)

	# update_darrc(daroot, artifacts)

if __name__ == "__main__":
	main()
