package org.chof.surfcomp.trimesh.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.chof.surfcomp.trimesh.exception.TrimeshException;

public abstract class SimpleSurfaceReader extends DefaultSurfaceReader {

	protected BufferedReader input = null;

	@Override
	public void setReader(Reader reader) throws TrimeshException {
	    if (reader instanceof BufferedReader) {
	        this.input = (BufferedReader)reader;
	    } else {
	        this.input = new BufferedReader(reader);
	    }
	}

	@Override
	public void setReader(InputStream reader) throws TrimeshException {
		setReader(new InputStreamReader(reader));
	}

	@Override
	public void close() throws IOException {
		if (input != null) {
			input.close();
		}
	
	}
}
