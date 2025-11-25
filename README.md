# Darman
Darman is a DAR (Digital Artifact Repository) manager. While conventional package managers and version control platforms provide ways to access their contents (git clone, pip install, etc) I wanted to create a tool that didn't care where a digital artifact was stored and for what purpose (software development, digital art, general file storage), and just pulled their respective repositories into a consistant storage pattern to be reliably accessed by other projects.

## How it works
Every DAR is expected to have 4 components usually displayed in the following format:

`{source}~{owner}/{repository}@{version}`

For example:

`github~nctsiridis/hii@v1.0.0`

On the tool user's machine, you must define a `daroot` within `~/.darrc`. The DAR will then be stored under the directory `{daroot}/{source}~{owner}/{repository}@{version}`.

## Config
Darman requires you to create a file called `~/.darrc`. The only manual configuration is to provide the daroot by inserting the following line:

`root {path}`

Every DAR you pull will automatically create an entry for that DAR in `~./darrc`.

## Commands
TODO
