package org.mdevlamynck.qttt.common.network.messages;

public enum ELobby {

	// Server Internal --------------------------------------------------------
	ADD_CLIENT,
	REMOVE_CLIENT,
	// ------------------------------------------------------------------------

	CLIENT_NAME,			/// (Game)		param : name

	REQUEST_CLIENT_LIST,	/// (Game)		reply : REPLY_CLIENT_LIST
	REPLY_CLIENT_LIST,		/// (Server)	param : map<id, GameSession>

	REQUEST_SESSION_LIST,	/// (Game)		reply : REPLY_SESSION_LIST
	REPLY_SESSION_LIST,		/// (Server)	param : map<id, GameSession>

	CREATE_SESSION,			/// (Game)		param : name
	CANCEL_SESSION,			/// (Game)

	CONNECT_TO_GAME,		/// (Game)		param : name
	OPPONENT_CONNECTED,		/// (Server)	param : name

	START,

	NONE

}
