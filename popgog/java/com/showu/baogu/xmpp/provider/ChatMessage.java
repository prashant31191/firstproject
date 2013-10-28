/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.showu.baogu.xmpp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Convenience definitions for NotePadProvider
 */
public final class ChatMessage {
	public static final String AUTHORITY = "com.baogu.showu.provider";

	// This class cannot be instantiated
	private ChatMessage() {
	}

	/**
	 * Notes table
	 */
	public static final class Message implements BaseColumns {
		// This class cannot be instantiated
		private Message() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/chat_message");
        public static  final String TABLE_NAME="message" ;
		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.showu.baogu";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.showu.baogu";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "TIME DESC";
		
		public static final String ID="ID" ;
		public static final String TARGET_USER_ID = "TARGET_USER_ID";
		public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
		public static final String USER_ID= "USER_ID";
        public static final String BASE_INFO= "BASE_INFO";
        public static final String MESSAGE_BODY= "MESSAGE_BODY";
        public static final String MESSAGE_SRC= "MESSAGE_SRC";
        public static final String MESSAGE_SEND_TIME="TIME" ;
        public static final String ROOM="ROOM";
        public static final String MESSAGE_STATUS="STATUS" ;
        public static final String LOCAL_PATH="LOCAL_PATH" ;
        public static final String HELP_TYPE="HELP_TYPE" ;
		/**
		 * The timestamp for when the note was created
		 * <P>
		 * Type: INTEGER (long from System.curentTimeMillis())
		 * </P>
		 */
		public static final String CREATED_DATE = "created";

		/**
		 * The timestamp for when the note was last modified
		 * <P>
		 * Type: INTEGER (long from System.curentTimeMillis())
		 * </P>
		 */
		public static final String MODIFIED_DATE = "modified";
	}
}
