/**
 * Copyright 2005-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.notes.service;

import java.io.File;
import java.util.List;

import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.Note;

/**
 * A service which handles data access for notes and attachments.
 * 
 * @see Note
 * @see Attachment
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NoteService {

    public Note getNoteByNoteId(String noteId);
    public List<Note> getNotesByDocumentId(String documentId);
    public Note saveNote(Note note);
    public void deleteNote(Note note);
    public void deleteAttachment(Attachment attachment);
    public File findAttachmentFile(Attachment attachment);
    public Attachment findAttachment(String attachmentId);
    
}
