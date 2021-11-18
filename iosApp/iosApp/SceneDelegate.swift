import SwiftUI
import UIKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = scene as? UIWindowScene else { return }

        let storyboard = UIStoryboard(name: "ViewController", bundle: nil)
        let vc = storyboard.instantiateInitialViewController()

        let window = UIWindow(windowScene: windowScene)
        window.rootViewController = vc
        self.window = window
        window.makeKeyAndVisible()
    }
}
