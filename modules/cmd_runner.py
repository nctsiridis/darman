import wget
import zipfile
import os
from modules.github import Github
from types import MethodType
import shutil

class CmdRunner:
	def __init__(self, daroot: str, artifacts: list, argv: list):
		self.daroot: str = daroot
		self.artifacts: list = artifacts
		self.argv = argv
		# FUTURE: make this separate file and parse in
		self.command_tree = {
			"ls": {"*": self.ls_cmd, "": self.ls_cmd},
			"pull": {"*": self.pull_cmd},
			"search": {"*": self.search_cmd},
			"help": self.help_cmd,
			"rm": {"*": self.rm_cmd}
		}
		self.github = Github(self.daroot, self.artifacts)

	def run(self):
		pos: dict = self.command_tree
		for arg in self.argv[1:]:
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
		if type(pos) != MethodType:
			if not isinstance(pos, dict) or pos.get("") == None:
				print("Invalid command")
				return
			else:
				pos = pos[""]
			print(f"new type: {type(pos)}")
			if type(pos) != MethodType:
				print("Invalid command")
				return
		pos()

	def ls_cmd(self):
		print("ls command is not implemented")
		pass
		
	def pull_cmd(self):
		# TODO check if already in artifacts, then if need to pull add to artifacts list
		if self.daroot == None:
			print("Please set daroot in ~/.darrc to use pull")
			return
		query = self.argv[-1]
		source = None
		if "~" in query:
			source, query = query.split("~")
		username, tagged_repo = query.split("/")
		repo, tag = tagged_repo.split("@")
		if source == "github":
			res = self.github.pull_repo(username, repo, tag)
			if (res):
				print("\n\nsuccess")
				self.artifacts.append(f"github~{query}")
			else:
				print("\n\nfailed")
		else:
			print(f"source '{source}' not recognized")

	def search_cmd(self):
		query = self.argv[-1]
		if "/" not in query:
			self.github.search_username(query)
		elif "@" not in query:
			username, repo = query.split("/")
			self.github.search_repo(username, repo)
		else:
			username, tagged_repo = query.split("/")
			repo, tag = tagged_repo.split("@")
			self.github.search_repo(username, repo, tag=tag)

	def rm_cmd(self):
		query = self.argv[-1]
		path = f"{self.daroot}/{query}"
		if query in self.artifacts and os.path.isdir(path):
			shutil.rmtree(path)
			# TODO make artifacts a set
			self.artifacts = [x for x in self.artifacts if x != query]
			print(f"Removed: {query}")
			a, b = query.split("/")
			path2 = f"{self.daroot}/{a}"
			if not os.listdir(path2):
				os.rmdir(path2)
		else:
			print(f"Artifact {query} not found")

	def help_cmd(self):
		print("Help command not implemented")


