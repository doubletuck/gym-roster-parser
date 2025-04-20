# Troubleshooting
Random issues encountered during development.

### Good ole ^M in files
The generated roster scraping exports from the college team rosters had the ^M character appearing.

To see the hidden `^M` character, run:
```shell
cat -v <filename>
```

To remove the `^M` character, run:
```shell
dos2unix <filename>
```

Or, for the entire directory:
```shell
dos2unix *
```

To fix the export, the line separator was set explicitly to `\n`.