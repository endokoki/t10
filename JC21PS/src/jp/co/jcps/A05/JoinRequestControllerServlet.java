package jp.co.jcps.A05;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.jcps.Bean.TrnJoinRequestBean;
import jp.co.jcps.Common.CommonCheck;
import jp.co.jcps.Common.DBConnection;

/**
 * 部員登録申請画面のコントローラー
 */
@WebServlet("/JoinRequestController")
public class JoinRequestControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public JoinRequestControllerServlet() {
		super();
	}

	/**
	 * GETメソッドでリクエストされた場合の処理
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 共通チェック
		if(!CommonCheck.existSession(request)) {
			// セッションが切れてる場合はログイン画面に遷移
			request.getRequestDispatcher("/Login").forward(request, response);
		}

		// セッションからログイン中のユーザーIDを取得する
		String userId = (String)request.getSession().getAttribute("userId");

		// SQLに埋め込むパラメータリストを定義
		List<String> paramList = new ArrayList<String>();
		paramList.add(userId);
		paramList.add(userId);

		// SQLを設定
		String sql = "SELECT * FROM mst_club WHERE club_id NOT IN (SELECT club_id FROM trn_join_request WHERE user_id = ?) AND club_id NOT IN (SELECT club_id FROM trn_club_member WHERE user_id = ?);";

		// SQLを実行し結果を取得
		DBConnection db = new DBConnection();
		ResultSet rs = db.executeSelectQuery(sql, paramList);

		// 部員登録申請画面に表示するbeanを初期化
		JoinRequestBean bean = new JoinRequestBean();

		try {
			// beanに部活名をセット
			while(rs.next()) {
				TrnJoinRequestBean data = new TrnJoinRequestBean();
				data.setClubId(rs.getString("club_id"));
				data.setClubName(rs.getString("club_name"));
				data.setClubDescription(rs.getString("club_description"));
				bean.addJoinRequestList(data);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new ServletException(e);
		} finally {
			try {
				db.close();
			} catch (Exception e) {
			}
		}

		// beanをリクエストにセット
		request.setAttribute("bean", bean);


		// 部活情報登録画面を表示
		request.getRequestDispatcher("A05/JoinRequest.jsp").forward(request, response);
	}

}