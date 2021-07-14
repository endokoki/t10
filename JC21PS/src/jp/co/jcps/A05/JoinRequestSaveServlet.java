package jp.co.jcps.A05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.jcps.Bean.MessageBean;
import jp.co.jcps.Common.CommonCheck;
import jp.co.jcps.Common.DBConnection;

/**
 * 部員登録申請画面の登録処理
 */
@WebServlet("/JoinRequestSave")
public class JoinRequestSaveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public JoinRequestSaveServlet() {
		super();

	}

	/**
	 * POSTでリクエストされた場合
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 共通チェック
		if (!CommonCheck.existSession(request)) {
			// セッションが切れてる場合はログイン画面に遷移
			request.getRequestDispatcher("/Login").forward(request, response);
		}
		//リクエストのエンコードを指定
		request.setCharacterEncoding("UTF-8");


		// TODO: データベースにデータを登録する為のSQL文を完成させなさい。
		String sql = "INSERT INTO";

		// SQLに埋め込むパラメータリストを定義
		List<String> paramList = new ArrayList<String>();
		/* TODO: SQLに埋め込む値をparamListに設定しなさい。
		 * ヒント①
		 * user_idはセッション情報から取得する。
		 * 取得の仕方はJoinRequestControllerServlet.java 43行目を参照
		 * ヒント②
		 * 登録する部活IDはリクエストパラメータとしてHttpServletRequestに格納されている。
		 * リクエストパラメータの取得のrequest.getParameter(【HTMLのname属性の値】)で取得可能
		 * A04,ParticipantListControllerServlet.java 43行目を参照
		 */



		// SQLを実行しデータを登録
		DBConnection db = new DBConnection();
		try {
			db.executeInsertUpdateQuery(sql, paramList);
		} catch (Exception e) {
			request.getRequestDispatcher("ERROR/Error.jsp").forward(request, response);
		}

		// msgに登録完了メッセージをセット
		MessageBean msg = new MessageBean();
		msg.addMessageList("部員登録申請をしました。");
		request.setAttribute("messageBean", msg);

		// TOP画面の呼び出し
		request.getRequestDispatcher("/TopController").forward(request, response);
	}
}
