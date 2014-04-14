package org.mdevlamynck.qttt.common.network.messages;

public enum ELobby {

	// Server Internal --------------------------------------------------------
	ADD_CLIENT,
	REMOVE_CLIENT,
	// ------------------------------------------------------------------------

	REQUEST_SESSION_LIST,	/// (Game)		param : map<id, GameSession>

	CREATE_SESSION,			/// (Game)		param : name
	CANCEL_SESSION,			/// (Game)

	CONNECT_TO_GAME,		/// (Game)		param : id
	OPPONENT_CONNECTED,		/// (Server)	param : id

	NONE

}
