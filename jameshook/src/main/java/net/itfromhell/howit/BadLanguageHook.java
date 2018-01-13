/**
 * Copyright 2018 Gabriel Stelmach
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the 
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Licensed under the MIT license: https://opensource.org/licenses/MIT
 */
package net.itfromhell.howit;

import net.itfromhell.howit.jameshook.precommit.PreCommitException;
import net.itfromhell.howit.jameshook.precommit.PreCommitHook;
import net.itfromhell.howit.jameshook.precommit.PreCommitInterdiction;

/**
 * Example of pre-commit hook to verify the contents of a committed file.
 * 
 * @author Gabriel Stelmach <gabriel_stelmach@hotmail.com>
 */
public class BadLanguageHook extends PreCommitInterdiction implements PreCommitHook
{
	/**
	 * List of forbidden words
	 */
	private final String bannedWords[] = {"bad luck", "crocodile", "peter pan"};
	
	/**
	 * Connects to SVN looking for forbidden words inside of the sent file.
	 * 
	 * @param args Array of arguments used to execute the hook. This hook expect the following sequence of arguments:
	 * <ol>
	 * <li>Fully qualified class name of this hook</li>
	 * <li>Repository's local path</li>
	 * <li>Current SVN transaction</li>
	 * <li>Flag for performed action</li>
	 * <li>File path</li>
	 * </ol>
	 * 
	 * @throws PreCommitException Indicates the presence of forbidden words in file contents.
	 */
	@Override
	public void validate(String[] args) throws PreCommitException
	{
		String contents = loadFileContents(args[1], getUsername(), getPassword(), args[2], args[4]).toUpperCase();
		
		for (String word : bannedWords)
		{
			log.debug("Looking for: " + word);
			if (contents.contains((word.toUpperCase())))
			{
				throw new PreCommitException("Is not acceptable the use of " + word + " in contents of files.");
			}
		}
	}
}